package unithon.helpjob.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSBundle
import platform.Foundation.NSIndexPath
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile
import platform.UIKit.*
import platform.darwin.NSInteger
import platform.darwin.NSObject

/**
 * iOS implementation of [rememberPlatformActions].
 */
@Composable
actual fun rememberPlatformActions(): PlatformActions {
    return remember {
        object : PlatformActions {
            @OptIn(ExperimentalForeignApi::class)
            override fun openOssLicenses() {
                val rootVC = findRootViewController() ?: run {
                    println("Unable to find root view controller")
                    return
                }

                val licenses = loadLicenses()
                if (licenses.isEmpty()) {
                    println("No licenses found")
                    return
                }

                val licensesListVC = LicensesListViewController(licenses)
                val navController = UINavigationController(rootViewController = licensesListVC)

                rootVC.presentViewController(navController, animated = true, completion = null)
            }

            @OptIn(ExperimentalForeignApi::class)
            private fun findRootViewController(): UIViewController? {
                val scenes = UIApplication.sharedApplication.connectedScenes
                val windowScene = scenes.firstOrNull() as? UIWindowScene
                val window = windowScene?.windows?.firstOrNull() as? UIWindow
                return window?.rootViewController()
            }

            @OptIn(ExperimentalForeignApi::class)
            private fun loadLicenses(): List<LicenseEntry> {
                val metadataPath = NSBundle.mainBundle.pathForResource("third_party_license_metadata", ofType = "txt")
                    ?: return emptyList()
                val licensesPath = NSBundle.mainBundle.pathForResource("third_party_licenses", ofType = "txt")
                    ?: return emptyList()

                val metadataText = NSString.stringWithContentsOfFile(
                    path = metadataPath,
                    encoding = NSUTF8StringEncoding,
                    error = null
                ) as? String ?: return emptyList()

                val licensesText = NSString.stringWithContentsOfFile(
                    path = licensesPath,
                    encoding = NSUTF8StringEncoding,
                    error = null
                ) as? String ?: return emptyList()

                return parseLicenses(metadataText, licensesText)
            }

            private fun parseLicenses(metadata: String, licensesText: String): List<LicenseEntry> {
                val entries = mutableListOf<LicenseEntry>()
                val lines = metadata.split("\n").filter { it.isNotBlank() }

                for (line in lines) {
                    val parts = line.split(" ", limit = 2)
                    if (parts.size < 2) continue

                    val positions = parts[0].split(":")
                    if (positions.size < 2) continue

                    val start = positions[0].toIntOrNull() ?: continue
                    val length = positions[1].toIntOrNull() ?: continue
                    val name = parts[1]

                    val licenseText = if (start + length <= licensesText.length) {
                        licensesText.substring(start, start + length)
                    } else {
                        "License text not available"
                    }

                    entries.add(LicenseEntry(name, licenseText))
                }

                return entries.sortedBy { it.name }
            }
        }
    }
}

/**
 * Data class for license entry
 */
data class LicenseEntry(
    val name: String,
    val licenseText: String
)

/**
 * View controller for displaying list of libraries
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class LicensesListViewController(
    private val licenses: List<LicenseEntry>
) : UIViewController(nibName = null, bundle = null) {

    private lateinit var tableView: UITableView

    private val closeButtonTarget = object : NSObject() {
        @ObjCAction
        fun closeTapped() {
            this@LicensesListViewController.dismissViewControllerAnimated(true, null)
        }
    }

    private val dataSource = object : NSObject(), UITableViewDataSourceProtocol {
        override fun tableView(tableView: UITableView, numberOfRowsInSection: NSInteger): NSInteger {
            return licenses.size.toLong()
        }

        override fun tableView(tableView: UITableView, cellForRowAtIndexPath: NSIndexPath): UITableViewCell {
            val cell = tableView.dequeueReusableCellWithIdentifier("LicenseCell")
                ?: UITableViewCell(UITableViewCellStyle.UITableViewCellStyleDefault, "LicenseCell")

            val license = licenses[cellForRowAtIndexPath.row.toInt()]
            cell.textLabel?.setText(license.name)
            cell.setAccessoryType(UITableViewCellAccessoryType.UITableViewCellAccessoryDisclosureIndicator)

            return cell
        }
    }

    private val delegate = object : NSObject(), UITableViewDelegateProtocol {
        override fun tableView(tableView: UITableView, didSelectRowAtIndexPath: NSIndexPath) {
            tableView.deselectRowAtIndexPath(didSelectRowAtIndexPath, animated = true)

            val license = licenses[didSelectRowAtIndexPath.row.toInt()]
            val detailVC = LicenseDetailViewController(license)
            this@LicensesListViewController.navigationController?.pushViewController(detailVC, animated = true)
        }
    }

    override fun viewDidLoad() {
        super.viewDidLoad()

        title = "Open Source Licenses"
        view.setBackgroundColor(UIColor.systemBackgroundColor)

        // Close button
        navigationItem.leftBarButtonItem = UIBarButtonItem(
            title = "Close",
            style = UIBarButtonItemStyle.UIBarButtonItemStylePlain,
            target = closeButtonTarget,
            action = platform.objc.sel_registerName("closeTapped")
        )

        // Create table view
        tableView = UITableView(frame = view.bounds, style = UITableViewStyle.UITableViewStylePlain)
        tableView.setTranslatesAutoresizingMaskIntoConstraints(false)
        tableView.setDataSource(dataSource)
        tableView.setDelegate(delegate)
        view.addSubview(tableView)

        // Setup constraints
        NSLayoutConstraint.activateConstraints(listOf(
            tableView.topAnchor.constraintEqualToAnchor(view.topAnchor),
            tableView.leadingAnchor.constraintEqualToAnchor(view.leadingAnchor),
            tableView.trailingAnchor.constraintEqualToAnchor(view.trailingAnchor),
            tableView.bottomAnchor.constraintEqualToAnchor(view.bottomAnchor)
        ))
    }
}

/**
 * View controller for displaying license detail
 */
@OptIn(ExperimentalForeignApi::class)
private class LicenseDetailViewController(
    private val license: LicenseEntry
) : UIViewController(nibName = null, bundle = null) {

    override fun viewDidLoad() {
        super.viewDidLoad()

        title = license.name
        view.backgroundColor = UIColor.systemBackgroundColor

        // Create text view
        val textView = UITextView()
        textView.setEditable(false)
        textView.setText(license.licenseText)
        textView.setFont(UIFont.systemFontOfSize(14.0))
        textView.setTranslatesAutoresizingMaskIntoConstraints(false)
        view.addSubview(textView)

        // Setup constraints
        NSLayoutConstraint.activateConstraints(listOf(
            textView.topAnchor.constraintEqualToAnchor(view.safeAreaLayoutGuide.topAnchor),
            textView.leadingAnchor.constraintEqualToAnchor(view.leadingAnchor, constant = 16.0),
            textView.trailingAnchor.constraintEqualToAnchor(view.trailingAnchor, constant = -16.0),
            textView.bottomAnchor.constraintEqualToAnchor(view.bottomAnchor)
        ))
    }
}