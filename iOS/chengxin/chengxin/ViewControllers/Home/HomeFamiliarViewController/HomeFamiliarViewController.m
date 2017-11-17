//
//  HomeFamiliarViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeFamiliarViewController.h"
#import "HomeFamiliarTableViewCell.h"
#import "Global.h"

@interface HomeFamiliarViewController ()

@end

@implementation HomeFamiliarViewController

@synthesize slideFamiliarScrollView, slideFamiliarPageCtrl, homeFamiliarTableView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.homeFamiliarTableView registerNib:[UINib nibWithNibName:@"HomeFamiliarTableViewCell" bundle:nil] forCellReuseIdentifier:@"HomeFamiliarCellIdentifier"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
   }

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // Set slide page time
    [self showSlideShow];
    slideFamiliarPageTimer = [NSTimer scheduledTimerWithTimeInterval:SLIDE_SECOND target:self selector:@selector(PageMove) userInfo:nil repeats:YES];
}

#pragma mark - Page Slide
- (void)showSlideShow {
    slideFamiliarScrollView.contentSize = CGSizeMake(slideFamiliarScrollView.frame.size.width * NUMBER_OF_SLIDE_IMAGES, slideFamiliarScrollView.frame.size.height);
    CGRect ViewSize = slideFamiliarScrollView.bounds;
    for(int i = 0; i <= NUMBER_OF_SLIDE_IMAGES; i++)
    {
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:ViewSize];
        imageView.contentMode = UIViewContentModeScaleToFill;
        [imageView setImage:[UIImage imageNamed:[NSString stringWithFormat:@"%d.png", (FAMILIAR_IMAGE_START + i)]]];
        [slideFamiliarScrollView addSubview:imageView];
        ViewSize = CGRectOffset(ViewSize, slideFamiliarScrollView.bounds.size.width, 0);
    }
}
- (void)PageMove {
    CGFloat pageSize = slideFamiliarScrollView.frame.size.width;
    NSInteger nCurrentPage = 0;
    // if this is the last page return
    if(slideFamiliarScrollView.contentOffset.x >= slideFamiliarScrollView.frame.size.width * NUMBER_OF_SLIDE_IMAGES) {
        [slideFamiliarScrollView setContentOffset:CGPointMake(0.0, slideFamiliarScrollView.contentOffset.y) animated:YES];
    } else {
        [slideFamiliarScrollView setContentOffset:CGPointMake(slideFamiliarScrollView.contentOffset.x + pageSize, slideFamiliarScrollView.contentOffset.y) animated:YES];
        nCurrentPage = (NSInteger)(slideFamiliarScrollView.contentOffset.x / slideFamiliarScrollView.frame.size.width) + 1;
    }
    [slideFamiliarPageCtrl setCurrentPage:nCurrentPage];
}

#pragma mark - TableView
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HomeFamiliarTableViewCell *homeFamiliarTableCell = (HomeFamiliarTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"HomeFamiliarCellIdentifier" forIndexPath:indexPath];
    return homeFamiliarTableCell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 5;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat homeTableCellHeight = 220.f;
    return homeTableCellHeight;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
